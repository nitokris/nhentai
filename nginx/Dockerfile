# 构建阶段
FROM node:22-alpine AS builder

WORKDIR /app

# 拷贝项目文件，
COPY webui/package.json webui/quasar.config.ts webui/postcss.config.js webui/index.html webui/tsconfig.json ./

RUN yarn install


COPY webui/ ./

RUN yarn build


# 运行阶段
FROM nginx:alpine

COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=builder /app/dist/spa /usr/share/nginx/html
