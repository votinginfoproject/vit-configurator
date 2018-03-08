FROM clojure:lein-2.6.1
MAINTAINER Democracy Works, Inc. <dev@democracy.works>

RUN mkdir -p /vit-configurator
WORKDIR /vit-configurator

COPY project.clj /vit-configurator/
# lein build before copying code over in order to cache the build dependencies
RUN lein cljsbuild once min

COPY . /vit-configurator/

CMD lein build
