import { DateObject } from './types/date';
import dayJs from 'dayjs';

class DateObjectInstance implements DateObject {
  format(timestamp: number | string | Date, pattern?: string): string {
    if (!pattern) {
      pattern = 'YYYY-MM-DD HH:mm:ss';
    }
    return dayJs(timestamp).format(pattern);
  }

  timestamp(date?: string | Date): number {
    if (!date) {
      date = new Date();
    }

    let parseDate: Date;
    if (date instanceof Date) {
      parseDate = date as Date;
    } else {
      parseDate = new Date(date);
    }

    return parseDate.getTime();
  }
}

export default DateObjectInstance;
