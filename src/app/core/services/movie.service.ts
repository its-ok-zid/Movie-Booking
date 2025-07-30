import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../../movies/movies.component';

@Injectable({ providedIn: 'root' })
export class MovieService {
  constructor(private http: HttpClient) {}

  getAllMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>('/api/v1.0/moviebooking/all');
  }

  searchMovieByName(movieName: string): Observable<Movie | null> {
    return this.http.get<Movie>(`/api/v1.0/moviebooking/movies/search/${encodeURIComponent(movieName)}`);
  }

  bookTicket(payload: {
    movieName: string;
    theatreName: string;
    numberOfTickets: number;
    seatNumbers: string;
  }): Observable<any> {
    return this.http.post(`/api/v1.0/moviebooking/${encodeURIComponent(payload.movieName)}/add`, payload);
  }
}
