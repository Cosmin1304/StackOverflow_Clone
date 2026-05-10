import { User } from './user.model';
import { Question } from './question.model';
import { Answer } from './answer.model';
import { Role } from './role.model';
import { Tag } from './tag.model';

// 1. Definim Rolurile și Tag-urile de bază
const roleUser: Role = { id: 1, roleName: 'USER' };
const roleMod: Role = { id: 2, roleName: 'MODERATOR' };

const tagCss: Tag = { id: 1, name: 'css' };
const tagHtml: Tag = { id: 2, name: 'html' };
const tagAngular: Tag = { id: 3, name: 'angular' };

// 2. Utilizatori de test
export const mockUsers: User[] = [
  {
    id: 1,
    username: 'cosmin_dev',
    email: 'cosmin@test.com',
    roles: [roleUser],
    score: 15,
    isBanned: false
  },
  {
    id: 2,
    username: 'admin_super',
    email: 'admin@test.com',
    roles: [roleUser, roleMod],
    score: 120,
    isBanned: false
  }
];

// 3. Întrebări de test (Atenție la createdAt și tags)
export const mockQuestions: Question[] = [
  {
    id: 1,
    author: mockUsers[0],
    title: 'Cum centrez un div în CSS?',
    text: 'Mă chinui de două ore să pun un div exact pe mijlocul ecranului. Am încercat margin: auto dar nu merge pe verticală. Ajutor?',
    createdAt: '2026-05-01T10:00:00', // Format ISO ca în Java
    status: 'SOLVED',
    tags: [tagCss, tagHtml],          // Acum folosim obiectele Tag
    voteCount: 5
  },
  {
    id: 2,
    author: mockUsers[1],
    title: 'Care e diferența dintre Angular și React?',
    text: 'Trebuie să aleg un framework pentru un proiect nou și nu știu pe care să merg. Păreri?',
    createdAt: '2026-05-05T14:30:00',
    status: 'RECEIVED',
    tags: [tagAngular],
    voteCount: 2
  }
];

// 4. Răspunsuri de test
export const mockAnswers: Answer[] = [
  {
    id: 1,
    topicId: 1,
    author: mockUsers[1],
    text: 'Cea mai simplă metodă modernă este să folosești Flexbox.',
    createdAt: '2026-05-01T10:15:00',
    voteCount: 10
  }
];
