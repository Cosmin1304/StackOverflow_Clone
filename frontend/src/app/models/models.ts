export interface UserResponseDTO {
  id: number;
  username: string;
  email: string;
  roles: string[];
  score: number;
  isBanned: boolean;
}

export interface UserRequestDTO {
  username?: string;
  email?: string;
  password?: string;
  phoneNumber?: string;
}

export interface LoginRequestDTO {
  username?: string;
  password?: string;
}

export interface TagDTO {
  id: number;
  name: string;
}

export interface VoteDTO {
  userId: number;
  voteType: string;
}

export interface TopicResponseDTO {
  id: number;
  title: string;
  text: string;
  pictureUrl?: string;
  author: UserResponseDTO;
  createdAt: string;
  voteCount: number;
  status: string;
  tags: TagDTO[];
  votes?: VoteDTO[];
}

export interface TopicRequestDTO {
  title: string;
  text: string;
  pictureUrl?: string;
  tagNames?: string[];
}

export interface AnswerResponseDTO {
  id: number;
  text: string;
  pictureUrl?: string;
  author: UserResponseDTO;
  voteCount: number;
  isAccepted: boolean;
  createdAt: string;
  votes?: VoteDTO[];
}

export interface AnswerRequestDTO {
  text: string;
  pictureUrl?: string;
}
