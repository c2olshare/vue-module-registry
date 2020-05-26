import Vue from 'vue';
import Vuex from 'vuex';
import utils from '@/lib/utils/utils';
import { AUTH_HEADER, EMPTY_NAMESPACE } from '@/lib/global/constant';
import { Namespace } from '@/lib/types/data';

Vue.use(Vuex);

interface State {
  accessToken: string | null;
  namespace: Namespace | null;
}

export { State };

export default new Vuex.Store({
  state: {
    accessToken: null,
    namespace: EMPTY_NAMESPACE
  },
  mutations: {
    updateNamespace(state: State, namespace: Namespace) {
      state.namespace = namespace;
    },
    setAccessToken(state: State, accessToken: string) {
      state.accessToken = accessToken;
      utils.setSessionStorageItem(AUTH_HEADER, accessToken);
    },
    removeAccessToken(state: State) {
      state.accessToken = null;
      utils.removeSessionStorageItem(AUTH_HEADER);
    }
  },
  actions: {},
  getters: {
    getCurrentNamespace(state: State) {
      return state.namespace;
    }
  },
  modules: {}
});
