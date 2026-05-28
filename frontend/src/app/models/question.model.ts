import { User } from './user.model';
import { Tag } from './tag.model';

export interface Question {
  id: number;
  author: User;
  title: string;
  text: string;
  createdAt: string;
  pictureUrl?: string;
  status: 'RECEIVED' | 'IN_PROGRESS' | 'SOLVED';
  tags: Tag[];
  voteCount: number;
}
