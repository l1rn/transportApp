FROM node:18 AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci --silent
COPY . .

RUN npm run build

FROM nginx:alpine
RUN rm -rf /etc/nginx/conf.d/*

COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist /usr/share/nginx/html
EXPOSE 80
CMD [ "nginx", "-g", "daemon off;" ]