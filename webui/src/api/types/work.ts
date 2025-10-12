export interface Work {
  id: number;
  title: string
  description: string
  resources: string[]
  previews?: string[]
  isFavourite: boolean
  isStar: boolean
  magnets?: []
  actors?: string
  tags?: string[],
  circle?: string
  files?: WorkFileInfo[]
}

export interface WorkFileInfo {
  displayName: string,
  fileHash: string
}
