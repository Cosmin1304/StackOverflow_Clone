import { Role } from './role.model';

export interface User {
  id: number;
  username: string;
  email: string;
  roles: Role[];          // Modificat din string în array de obiecte Role
  score: number;
  isBanned: boolean;
}
