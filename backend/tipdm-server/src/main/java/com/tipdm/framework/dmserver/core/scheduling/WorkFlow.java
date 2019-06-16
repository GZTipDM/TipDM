package com.tipdm.framework.dmserver.core.scheduling;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tipdm.framework.common.utils.StringKit;
import com.tipdm.framework.dmserver.core.scheduling.model.Job;
import com.tipdm.framework.dmserver.core.scheduling.model.Link;
import com.tipdm.framework.dmserver.core.scheduling.model.Node;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.TreeSingleSourcePathsImpl;
import org.jgrapht.alg.util.Pair;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedAcyclicGraph;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author zhoulong E-mail:devp@tipdm.com
 * @version 创建时间：2016年11月7日 下午2:42:26
 *          类说明
 */
public class WorkFlow implements Serializable {

    private String id;

    private String creator;

    private Command command = Command.STARTALL;

    private List<Node> nodes;//节点

    private List<Link> links;//连接

    private Map<String, Job> jobMap;

    private Set<Dependency> dependencies;

    private DirectedAcyclicGraph<String, DefaultEdge> graph;

    public WorkFlow(){

    }

    public WorkFlow(String creator, String workFlowJSONContent) {
        this(StringKit.getBase64FromUUID(), creator, workFlowJSONContent);
    }

    public WorkFlow(String id, String creator, String workFlowJSONContent) {
        this(id, creator, workFlowJSONContent, null, null);
    }

    public WorkFlow(String id, String creator, String workFlowJSONContent, String nodeId, Command command) {
        this.dependencies = new HashSet<>();
        this.jobMap = new ConcurrentHashMap<>();
        this.creator = creator;
        this.id = id;
        this.command = command;
        this.graph = new DirectedAcyclicGraph<String, DefaultEdge>(Edge.class);
        if (nodeId != null) {
            parseFlowJSON(nodeId, command, workFlowJSONContent);
        } else {
            parseFlowJSON(workFlowJSONContent);
        }

    }

    /**
     * 解析工作流
     */
    private void parseFlowJSON(String content) {
        this.parseFlowJSON(null, null, content);
    }

    /**
     * 解析工作流
     */
    private void parseFlowJSON(String nodeId, Command command, String content) {
        JSONObject jsonObject = JSONObject.parseObject(content);
        this.nodes = JSON.parseArray(jsonObject.getString("nodes"), Node.class);
        this.links = JSON.parseArray(jsonObject.getString("links"), Link.class);

        for (Node node : this.nodes) {
            Job job = new Job();
            job.setJobId(node.getId());
            job.setJobName(node.getId());
            job.setJobGroup(this.id);
            job.setTargetClazz(node.getTargetAlgorithm());

            if(command == Command.ENDAT && node.getId().equals(nodeId)){
                job.setEndedNode(true);
            }
            jobMap.put(node.getId(), job);
            graph.addVertex(node.getId());
        }


        for (Job job : jobMap.values()) {
            Set<String> preJobs = this.links.stream().filter(x -> x.getTarget().equals(job.getJobId())).map(Link::getSource).collect(Collectors.toSet());
            for (String jobId : preJobs) {
                Dependency dependency = new Dependency(jobId, job.getJobId());
                dependencies.add(dependency);
                graph.addEdge(jobId, job.getJobId());
            }

            Set<String> afterJobs = this.links.stream().filter(x -> x.getSource().equals(job.getJobId())).map(Link::getTarget).collect(Collectors.toSet());
            for (String jobId : afterJobs) {
                Dependency dependency = new Dependency(job.getJobId(), jobId);
                dependencies.add(dependency);
                graph.addEdge(job.getJobId(), jobId);
            }
        }

        if (command == Command.STARTAT) {
            dependencies.clear();
            DijkstraShortestPath<String, DefaultEdge> dijkstraAlg =
                    new DijkstraShortestPath<>(graph);
            TreeSingleSourcePathsImpl<String, DefaultEdge> paths = (TreeSingleSourcePathsImpl<String, DefaultEdge>) dijkstraAlg.getPaths(nodeId);
            for (Map.Entry<String, Pair<Double, DefaultEdge>> entry : paths.getDistanceAndPredecessorMap().entrySet()) {
                Pair<Double, DefaultEdge> pair = entry.getValue();
                Edge edge = (Edge) pair.getSecond();
                if (null != edge) {
                    Dependency dependency = new Dependency((String) edge.getSource(), (String) edge.getTarget());
                    dependencies.add(dependency);
                }
            }
            graph = null;
        } else if (command == Command.ONLY) {
            dependencies.clear();
            Dependency dependency = new Dependency(nodeId, nodeId);
            dependencies.add(dependency);
        }
    }


    public String getId() {
        return id;
    }

    protected Set<Dependency> getDependencies() {
        return dependencies;
    }

    public String getCreator() {
        return creator;
    }

    protected List<Node> getNodes() {
        return nodes;
    }

    protected List<Link> getLinks() {
        return links;
    }

    protected Map<String, Job> getJobMap() {
        return this.jobMap;
    }

}
