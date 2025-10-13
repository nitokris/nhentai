export interface Magnet {
  url: string;
  title: string;
  type: string;
  size: string;
  date: string;
  isDownloaded: string;
  category: string;

  [key: string]: string;
}

export interface MagnetId {
  id: string
}
