import { UtilsObject } from './types/utils';

class UtilsInstance implements UtilsObject {
  obtainMessage(error: any, message: string): string {
    let result: string;
    if (error.response) {
      result = error.response.data ? error.response.data.message : null;
    } else {
      result = error.message;
    }
    return result || message;
  }

  setSessionStorageItem(key: string, data: any) {
    sessionStorage.setItem(key, JSON.stringify(data));
  }

  getSessionStorageItem(key: string) {
    const json = sessionStorage.getItem(key);
    if (json) {
      return JSON.parse(json);
    } else {
      return undefined;
    }
  }

  removeSessionStorageItem(key: string) {
    sessionStorage.removeItem(key);
  }

  setLocalStorageItem(key: string, data: any) {
    localStorage.setItem(key, JSON.stringify(data));
  }

  getLocalStorageItem(key: string) {
    const json = localStorage.getItem(key);
    if (json) {
      return JSON.parse(json);
    } else {
      return undefined;
    }
  }

  removeLocalStorageItem(key: string) {
    localStorage.removeItem(key);
  }

  replaceUrlParam(name: string, value: string) {
    const oldUrl = window.location.href.toString();
    const url = new URL(oldUrl);
    url.searchParams.set(name, value);
    window.history.replaceState(null, document.title, url.toString());
  }
}

export default new UtilsInstance();
