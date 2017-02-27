FROM reg.miz.so/library/tomcat:7.0.69-apm
MAINTAINER "Maizuo Qiyang <qiyang@hyx.com>"

ADD build/libs/springboot_demo.jar /usr/local/tomcat/demo/

CMD java -jar -Dspring.profiles.active=local /usr/local/tomcat/demo/springboot_demo.jar