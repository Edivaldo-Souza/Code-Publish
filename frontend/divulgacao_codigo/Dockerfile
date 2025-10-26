FROM node:22-alpine
COPY . /site
WORKDIR /site
RUN npm clean-install
RUN npm run build
CMD ["npm", "start"]