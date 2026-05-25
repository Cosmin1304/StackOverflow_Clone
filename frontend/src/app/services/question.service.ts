import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TopicResponseDTO, TopicRequestDTO, AnswerResponseDTO, AnswerRequestDTO } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/api/topics';
  private answersApiUrl = 'http://localhost:8080/api/answers';

  getQuestions(): Observable<TopicResponseDTO[]> {
    return this.http.get<TopicResponseDTO[]>(this.apiUrl);
  }

  addQuestion(topicDTO: TopicRequestDTO): Observable<TopicResponseDTO> {
    return this.http.post<TopicResponseDTO>(this.apiUrl, topicDTO);
  }

  voteQuestion(topicId: number, voteType: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${topicId}/vote?voteType=${voteType}`, {});
  }

  addAnswer(topicId: number, answerDTO: AnswerRequestDTO): Observable<AnswerResponseDTO> {
    return this.http.post<AnswerResponseDTO>(`${this.answersApiUrl}/topic/${topicId}`, answerDTO);
  }

  voteAnswer(answerId: number, voteType: string): Observable<void> {
    return this.http.post<void>(`${this.answersApiUrl}/${answerId}/vote?voteType=${voteType}`, {});
  }

  getQuestionById(id: number): Observable<TopicResponseDTO> {
    return this.http.get<TopicResponseDTO>(`${this.apiUrl}/${id}`);
  }

  getAnswersForQuestion(topicId: number): Observable<AnswerResponseDTO[]> {
    return this.http.get<AnswerResponseDTO[]>(`${this.answersApiUrl}/topic/${topicId}`);
  }

  deleteQuestion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateQuestion(id: number, topicDTO: TopicRequestDTO): Observable<TopicResponseDTO> {
    return this.http.put<TopicResponseDTO>(`${this.apiUrl}/${id}`, topicDTO);
  }

  deleteAnswer(id: number): Observable<void> {
    return this.http.delete<void>(`${this.answersApiUrl}/${id}`);
  }

  updateAnswer(id: number, newText: string): Observable<AnswerResponseDTO> {
    return this.http.put<AnswerResponseDTO>(`${this.answersApiUrl}/${id}?newText=${newText}`, {});
  }
}
