FROM node:18 AS builder

WORKDIR /app
COPY package*.json ./

RUN npm cache clean --force && \
    npm install --force --include=dev
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80