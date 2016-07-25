#Struts2

是一个用于开发MVC应用程序的框架

使用一个过滤器作为控制器

HTML表单被直接映射到一个POJO（普通java类

逻辑验证写在Action中


任何一个POJO都可以是一个Action

使用OGNL显示各个对象模型，可以不适用EL和JSTL

#Hello World

搭建Struts2环境

-加入jar包：复制struts\apps\struts2-blank\WEB-INF\lib下的所有jar包到当前web应用的lib下

-在web.xml中配置struts2：复制struts\apps\struts2-blank\WEB-INF\web.xml文件中的过滤器到当前web应用的web.xml

-在当前web应用的classpath下添加struts2配置文件struts.xml:复制struts\apps\struts2-blank\WEB-INF\classes下的struts.xml到web应用的src下


struts2.xml自动补全配置（配置dtd

-加入struts.xml后可复制http://struts.apache.org/dtds/struts-2.3.dtd到window->preferences->xml->xml catelog->add

-key type选url，key:http://struts.apache.org/dtds/struts-2.3.dtd,location选filesystem

-到struts\src\core\src\main\resources,找到对应的dtd约束文件，重新打开struts2.xml即可自动补全


-不需要显示定义Filter，使用Struts2配置文件

步骤

1.	由product-input.action转到/WEB-INF/pages/input.jsp
	
	在struts2中配置一个action
	
	<action name="product-input">
		<result>/WEB-INF/pages/input.jsp</result>
	</action>


2.	由input.jsp页面的action：product-save.action到Product's save,再到/WEB-INF/pages/details.jsp
	
	在product中定义一个save方法，且返回值为details 
