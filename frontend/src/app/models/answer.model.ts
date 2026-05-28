import { User } from './user.model';

export interface Answer {
  id: number;
  topicId: number;
  author: User;
  text: string;
  pictureUrl?: string;
  createdAt: string;
  voteCount: number;
}
