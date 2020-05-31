module.exports = {
  devServer: {
    port: 8080,
    proxy: 'http://127.0.0.1:8989'
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
