# rubrica-parent

######This is an example of telephone book with JSF + primefaces themes and JPA but in JSE 
######enviroment (Tomcat8, require JDK7/8)
######database: derby on file system

## deploy on tomcat 8
#
-copy in conf/context.xml:
#
            <Resource name="jdbc/TestappDS" auth="Container" type="javax.sql.DataSource"
            driverClassName="org.apache.derby.jdbc.ClientDriver" url="jdbc:derby://localhost:1527/.rubrica;create=true;"
            maxTotal="20" maxIdle="10" maxWait="-1" />
#
file .rubrica will be created empty in your user directory
-copy rubrica.war in webapps
#
home page:
http://localhost:8080/rubrica