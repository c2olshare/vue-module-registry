import Vue from 'vue';

interface UtilsObject {
  obtainMessage(error: any, message: string): string;
  setSessionStorageItem(key: string, data: any): void;
  getSessionStorageItem(key: string): any;
  removeSessionStorageItem(key: string): void;
  setLocalStorageItem(key: string, data: any): void;
  getLocalStorageItem(key: string): any;
  removeLocalStorageItem(key: string): void;
  replaceUrlParam(name: string, key: string): void;
}

declare module 'vue/types/vue' {
  interface Vue {
    $utils: UtilsObject;
  }
}
