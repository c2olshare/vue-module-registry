import { IconfontObject } from './types/icon';
import * as IconFont from './res/iconfont.json';

class IconfontInstance implements IconfontObject {
  iconMap = new Map<string, any>();

  constructor() {
    IconFont.glyphs.forEach(icon => {
      this.iconMap.set(icon.name, icon);
    });
  }

  getClass(name: string): string | undefined {
    const icon = this.getIcon(name);
    if (icon) {
      return `${IconFont.font_family} ${IconFont.css_prefix_text}${name}`;
    }
    return undefined;
  }

  getIcon(name: string): string | undefined {
    return this.iconMap.get(name);
  }

  listIcon(): any[] {
    return IconFont.glyphs;
  }
}

export default IconfontInstance;
