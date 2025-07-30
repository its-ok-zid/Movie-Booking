import { Component, OnInit } from '@angular/core';
import { MovieService } from '../core/services/movie.service';
import { HttpClient } from '@angular/common/http';

interface AdminMovie {
  movieName: string;
  theatreName: string;
  totalTickets: number;
  status: string;
  bookedTickets: number;
}

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.scss']
})
export class AdminDashboardComponent implements OnInit {
  movies: AdminMovie[] = [];
  loading = true;
  errorMsg = '';
  showErrorModal = false;
  errorModalMsg = '';
  editIndex: number | null = null;
  updatedTickets: number = 0;

  constructor(private movieService: MovieService, private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchMovies();
  }

  fetchMovies(): void {
    this.loading = true;
    this.movieService.getAllMovies().subscribe({
      next: (data: any[]) => {
        this.movies = data.map(m => ({
          movieName: m.movieName,
          theatreName: m.theatreName,
          totalTickets: m.totalTickets,
          status: m.status,
          bookedTickets: 0 // will fetch below
        }));
        this.fetchBookedTickets();
      },
      error: () => {
        this.errorMsg = 'Failed to load movies.';
        this.loading = false;
      }
    });
  }

  fetchBookedTickets(): void {
    let remaining = this.movies.length;
    this.movies.forEach((movie, idx) => {
      this.http.get<number>(`/api/v1.0/moviebooking/tickets/booked/${movie.movieName}/${movie.theatreName}`).subscribe({
        next: (count) => {
          this.movies[idx].bookedTickets = count;
          remaining--;
          if (remaining === 0) this.loading = false;
        },
        error: () => {
          this.movies[idx].bookedTickets = 0;
          remaining--;
          if (remaining === 0) this.loading = false;
        }
      });
    });
  }

  startEdit(index: number): void {
    this.editIndex = index;
    this.updatedTickets = this.movies[index].totalTickets;
  }

  saveTickets(index: number): void {
    const movie = this.movies[index];
    const payload = {
      movieName: movie.movieName,
      theatreName: movie.theatreName,
      totalTickets: this.updatedTickets
    };
    this.http.put(`/api/v1.0/moviebooking/${movie.movieName}/update/${movie.theatreName}`, payload).subscribe({
      next: () => {
        // Show toast and reload for guaranteed sync
        window.alert('Ticket count updated successfully!');
        this.fetchMovies();
        this.editIndex = null;
        this.errorMsg = '';
      },
      error: () => {
        this.errorMsg = 'Failed to update tickets.';
      }
    });
  }

  deleteMovie(index: number): void {
    const movie = this.movies[index];
    this.http.delete(`/api/v1.0/moviebooking/${movie.movieName}/delete/${movie.theatreName}`).subscribe({
      next: () => {
        window.alert('Movie deleted successfully!');
        this.fetchMovies();
        this.errorMsg = '';
      },
      error: (err) => {
        if (err.status === 409) {
          let msg = 'Bookings already exist for this movie. Cannot delete.';
          if (err.error && typeof err.error === 'string') {
            msg = err.error;
          }
          window.alert(msg);
        } else {
          this.errorMsg = 'Failed to delete movie.';
        }
      }
    });
  }

  cancelEdit(): void {
    this.editIndex = null;
  }

  closeErrorModal(): void {
    this.showErrorModal = false;
    this.errorModalMsg = '';
  }
}
