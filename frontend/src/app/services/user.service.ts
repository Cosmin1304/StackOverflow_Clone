import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { UserRequestDTO, UserResponseDTO } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/users';

  registerUser(userData: UserRequestDTO): Observable<UserResponseDTO> {
    return this.http.post<UserResponseDTO>(`${this.apiUrl}/register`, userData);
  }

  updateUser(id: number, updateData: UserRequestDTO): Observable<UserResponseDTO> {
    return this.http.put<UserResponseDTO>(`${this.apiUrl}/${id}`, updateData);
  }
}
