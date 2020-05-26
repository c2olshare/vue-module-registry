import Vue from 'vue';

interface DateObject {
  format(timestamp: number | string | Date, pattern?: string): string;

  timestamp(date?: string | Date): number;
}

declare module 'vue/types/vue' {
  interface Vue {
    $date: DateObject;
  }
}
