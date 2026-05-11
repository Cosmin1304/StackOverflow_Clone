import { Injectable } from '@angular/core';
import { Question } from '../models/question.model';
import { Answer } from '../models/answer.model';
import { mockQuestions, mockAnswers } from '../models/mock-data';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  constructor() { }

  // Funcție pentru a obține toate întrebările
  getQuestions(): Question[] {
    return mockQuestions;
  }

  // Funcție pentru a găsi o întrebare după ID
  getQuestionById(id: number): Question | undefined {
    return mockQuestions.find(q => q.id === id);
  }

  // Funcție pentru a obține răspunsurile unei întrebări
  getAnswersForQuestion(topicId: number): Answer[] {
    return mockAnswers.filter(a => a.topicId === topicId);
  }
}
