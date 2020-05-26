import Vue from 'vue';

interface IconfontObject {
  getIcon(name: string): string | undefined;

  getClass(name: string): string | undefined;

  listIcon(): string[];
}

declare module 'vue/types/vue' {
  interface Vue {
    $iconfont: IconfontObject;
  }
}
