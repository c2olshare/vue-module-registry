declare module 'vue-particles' {
  let VueParticles: any;
  export default VueParticles;
}

declare module 'qs' {
  let QS: {
    stringify: Function;
    parse: Function;
  };

  export default QS;
}
