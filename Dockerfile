FROM lycantropos/cmake as gos

RUN apt-get update
RUN apt-get install -y software-properties-common
RUN apt-add-repository 'deb http://security.debian.org/debian-security stretch/updates main'
RUN apt-get update
RUN apt-get install -y openjdk-8-jdk


RUN git clone https://github.com/roger21gm/GOS
WORKDIR /GOS
RUN mkdir build
WORKDIR /GOS/build
RUN cmake ..
RUN make


FROM maven:3.6.3-jdk-8

COPY --from=gos /GOS/build/CSP2SAT /server/gos

COPY . /server

WORKDIR /server

RUN mvn verify

RUN mkdir /tmp/models


ENTRYPOINT java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/compiler-0.0.1-SNAPSHOT.jar 
# ENTRYPOINT java -jar target/compiler-0.0.1-SNAPSHOT.jar






