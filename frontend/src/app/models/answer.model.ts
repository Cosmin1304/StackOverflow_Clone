import { User } from './user.model';

export interface Answer {
  id: number;
  topicId: number;        // În loc de questionId, pentru că în Java e Topic
  author: User;
  text: string;           // Datorita lui @JsonProperty("text") in Java
  pictureUrl?: string;
  createdAt: string;      // Modificat
  voteCount: number;
  // Aparent, în Java lipsește isAccepted. Îl putem adăuga mai târziu în backend
  // isAccepted: boolean;
}
