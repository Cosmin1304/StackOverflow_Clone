// --- MODELE PENTRU UTILIZATOR ---
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
}

export interface LoginRequestDTO {
  username?: string;
  password?: string;
}

// --- MODELE PENTRU TAG-URI ---
export interface TagDTO {
  id: number;
  name: string;
}

// --- MODELE PENTRU INTREBARI (TOPICS) ---
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
}

export interface TopicRequestDTO {
  title: string;
  text: string;
  pictureUrl?: string;
  tagNames?: string[];
}

// --- MODELE PENTRU RASPUNSURI (ANSWERS) ---
export interface AnswerResponseDTO {
  id: number;
  text: string;
  pictureUrl?: string;
  author: UserResponseDTO;
  voteCount: number;
  isAccepted: boolean;
  createdAt: string;
}

export interface AnswerRequestDTO {
  text: string;
  pictureUrl?: string;
}
