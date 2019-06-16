package com.tipdm.framework.model.dmserver;

import com.tipdm.framework.model.IdEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TipDM on 2016/12/9.
 * E-mail:devp@tipdm.com
 */
@Entity
@Table(name = "dm_element_tab")
public class ElementTab extends IdEntity<Long> {

    private static final long serialVersionUID = 3407102388232085114L;

    @Column(name = "tab_name", length = 20, nullable = false)
    private String tabName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "tab_id")
    @OrderBy("sequence ASC")
    private List<Element> elements = new ArrayList<>();

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
