#coding=utf-8
import base64
import pyh

class Report():
    # coding=utf-8
    def __init__(self):
        self.__report = pyh.PyH('算法运行报告')
        self.__report.addCSS('R2HTML.css')

    '''
    插入<h1>标签
    '''
    def h1(self, title):
        self.__report << pyh.h1(title)



    '''
    插入<h3>标签
    '''
    def h3(self, title):
        h3 = pyh.h3(title)
        h3.attributes['class'] = 'cls'
        self.__report << h3

    '''
    插入<p>标签
    '''
    def p(self, content):
        self.__report << pyh.p(content)
    
    '''
    插入<span>标签
    '''
    def span(self, content):
        self.__report << pyh.span(content)

    '''
    插入<pre>标签
    '''
    def pre(self, content):
        self.__report << pyh.pre(content)
    
    '''
    测试——加表格table
    ''' 
    def table(self, form):
        mytable = self.__report << pyh.table()
        n_row = form.index.size
        n_col = form.columns.size
        mytable << pyh.tr() 
        for i in range(n_col):
            mytable << pyh.th(str(form.columns[i]))
            
        for i in range(n_row):
            mytable << pyh.tr()
            for j in range(n_col):
                mytable << pyh.td(str(form.iloc[i,j]))

   
    
   
    
    

    '''
    插入<img>标签, 内容以base64格式存储
    '''

    def image(self, imageName):
        #imageFile = '/tmp/imageFile/' + imageName
        imageFile = imageName
        imageFile = imageFile.encode('utf-8')
        with open(imageFile, 'rb') as f:
            img_data = f.read()
        base64_data = base64.b64encode(img_data)
        img = pyh.img()
        img.attributes['style'] = "margin:0 auto;display:block;"
        img.attributes['src'] = 'data:image/png;base64,'+ base64_data.decode()
        self.__report << img

    '''
    插入<iframe>标签, 内容已base64格式存储
    '''

    def iframe(self, iframeName):
        #iframeFile = '/tmp/iframeFile/' + iframeName
        iframeFile = iframeName
        iframeFile = iframeFile.encode('utf-8')
        with open(iframeFile, 'rb') as f:
            data = f.read()
        base64_data = base64.b64encode(data)
        iframe = pyh.iframe()
        iframe.attributes['src'] = 'data:text/html;base64,'+ base64_data.decode()
        iframe.attributes['width'] = '100%'
        iframe.attributes['height'] = '100%'
        iframe.attributes['scrolling'] =" no"
        iframe.attributes['frameborder'] = "0"
        self.__report << iframe

    def writeToHtml(self, filePath):
        self.__report.printOut(filePath)
        
        
        

        
        