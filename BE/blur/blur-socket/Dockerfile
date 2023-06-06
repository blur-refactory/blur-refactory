FROM node:18.12.1-alpine

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install --silent

COPY . .

CMD ["npm", "run", "dev"]

EXPOSE 3000

