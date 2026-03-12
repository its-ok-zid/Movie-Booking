import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Movie } from '../../movies/movies.component';
import { environment } from 'src/environments/environment';

@Injectable({ providedIn: 'root' })
export class MovieService {
  constructor(private http: HttpClient) {}

  private readonly BASE_URL = environment.apiUrl + '/api/v1.0/moviebooking';

  getAllMovies(): Observable<Movie[]> {
    return this.http.get<Movie[]>(`${this.BASE_URL}/all`);
  }

  searchMovieByName(movieName: string): Observable<Movie | null> {
    return this.http.get<Movie>(`${this.BASE_URL}/movies/search/${encodeURIComponent(movieName)}`);
  }

  bookTicket(payload: {
    movieName: string;
    theatreName: string;
    numberOfTickets: number;
    seatNumbers: string;
  }): Observable<any> {
    return this.http.post(`${this.BASE_URL}/${encodeURIComponent(payload.movieName)}/add`, payload);
  }
}
