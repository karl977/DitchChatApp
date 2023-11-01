export type Stream = {
  id: number
  name: string,
  nid: string
  title: string,
  category: string,
  avatar_url: string,
  image_url: string,
  video_id: string,
  viewers: number,
};

type ChatMessageText = {
  type: string,
  text: string
}

export type ChatMessageType = {
  id: number,
  timestamp: string,
  badges: string[],
  username: string,
  color: string,
  messageContents: ChatMessageText[]
}
