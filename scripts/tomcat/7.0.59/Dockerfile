FROM lucasgmartins/java8
MAINTAINER Lucas Martins

ENV CATALINA_HOME /usr/local/tomcat
ENV PATH $CATALINA_HOME/bin:$PATH

RUN mkdir -p "$CATALINA_HOME"
RUN yum install -y tar

WORKDIR $CATALINA_HOME

ENV TOMCAT_MAJOR 7
ENV TOMCAT_VERSION 7.0.59
ENV TOMCAT_TGZ_URL https://www.apache.org/dist/tomcat/tomcat-$TOMCAT_MAJOR/v$TOMCAT_VERSION/bin/apache-tomcat-$TOMCAT_VERSION.tar.gz

RUN curl -SL "$TOMCAT_TGZ_URL" -o tomcat.tar.gz \
  && tar -xvf tomcat.tar.gz --strip-components=1 \
  && rm bin/*.bat \
  && rm tomcat.tar.gz*

EXPOSE 8080

CMD ["catalina.sh", "run"]