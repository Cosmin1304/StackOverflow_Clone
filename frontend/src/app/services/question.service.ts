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

  voteTopic(topicId: number, voteType: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${topicId}/vote?voteType=${voteType}`, {});
  }

  voteAnswer(answerId: number, voteType: string): Observable<any> {
    return this.http.post(`${this.answersApiUrl}/${answerId}/vote?voteType=${voteType}`, {});
  }

  addAnswer(topicId: number, answerDTO: AnswerRequestDTO): Observable<AnswerResponseDTO> {
    return this.http.post<AnswerResponseDTO>(`${this.answersApiUrl}/topic/${topicId}`, answerDTO);
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

  updateAnswer(answerId: number, data: any): Observable<any> {
    return this.http.put(`${this.answersApiUrl}/${answerId}`, data);
  }

  uploadImage(file: File): Observable<{ url: string }> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<{ url: string }>('http://localhost:8080/api/images/upload', formData);
  }
}
