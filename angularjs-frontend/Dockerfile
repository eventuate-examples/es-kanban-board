FROM node:0.12
RUN mkdir -p /app/angularjs-frontend
WORKDIR /app/angularjs-frontend
RUN npm install -g gulp
ADD ./package.json /app/angularjs-frontend/package.json
RUN  cd /app/angularjs-frontend ; npm install
RUN npm install --production
ADD . /app/angularjs-frontend
RUN gulp build-app
