module.exports = {
  devServer: {
    port: 80,
    proxy: 'http://127.0.0.1:8080'
  },
  css: {
    loaderOptions: {
      less: {
        javascriptEnabled: true
      }
    }
  },
  configureWebpack: {
    plugins: []
  }
};
