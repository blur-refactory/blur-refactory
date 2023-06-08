const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = (app) => {
  app.use(
    "/proxy",
    createProxyMiddleware({
      target: "http://192.168.31.73:8000",
      changeOrigin: true,
    })
  );
};
