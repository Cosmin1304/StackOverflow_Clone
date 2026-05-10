import { User } from './user.model';
import { Tag } from './tag.model';

export interface Question {
  id: number;
  author: User;
  title: string;
  text: string;           // Datorita lui @JsonProperty("text") in Java
  createdAt: string;
  pictureUrl?: string;
  status: 'RECEIVED' | 'IN_PROGRESS' | 'SOLVED';
  tags: Tag[];            // Modificat din array de stringuri
  voteCount: number;      // Acesta va fi calculat în backend (din TopicVote)
}
