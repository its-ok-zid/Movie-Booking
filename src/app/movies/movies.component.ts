import { Component, OnInit } from '@angular/core';
import { BookTicketModalComponent } from './book-ticket-modal.component';
import { MovieService } from '../core/services/movie.service';
import { Router } from '@angular/router';

export interface Movie {
  movieName: string;
  theatreName: string;
  totalTickets: number;
  status: string;
  imageUrl?: string;
}

@Component({
  selector: 'app-movies',
  templateUrl: './movies.component.html',
  styleUrls: ['./movies.component.scss']
})
export class MoviesComponent implements OnInit {
  movies: Movie[] = [];
  filteredMovies: Movie[] = [];
  loading = true;
  errorMsg = '';
  searchTerm: string = '';

  showBookingModal = false;
  selectedMovieName = '';
  selectedTheatreName = '';

  constructor(private movieService: MovieService, private router: Router) {}
  goToBookingPage(movie: Movie): void {
    this.router.navigate(['/book', movie.movieName, movie.theatreName], {
      queryParams: { totalTickets: movie.totalTickets }
    });
  }

  ngOnInit(): void {
    const role = localStorage.getItem('role');
    if (role !== 'USER') {
      // If not USER, redirect to login or admin
      if (role === 'ADMIN') {
        this.router.navigate(['/admin']);
      } else {
        this.router.navigate(['/login']);
      }
      return;
    }
    this.getAllMovies();
  }

  getAllMovies(): void {
    this.loading = true;
    this.movieService.getAllMovies().subscribe({
      next: (data) => {
        this.movies = data.map((m: Movie) => ({
          ...m,
          imageUrl: '/assets/movie-placeholder.jpg'
        }));
        this.filteredMovies = this.movies;
        this.loading = false;
      },
      error: (err) => {
        this.errorMsg = 'Failed to load movies.';
        this.loading = false;
      }
    });
  }

  onSearchTermChange(): void {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      this.filteredMovies = this.movies;
      this.errorMsg = '';
      return;
    }
    this.filteredMovies = this.movies.filter(m =>
      m.movieName.toLowerCase().includes(term)
    );
    this.errorMsg = this.filteredMovies.length ? '' : 'No movie found.';
  }

  openBookingModal(movie: Movie): void {
    this.selectedMovieName = movie.movieName;
    this.selectedTheatreName = movie.theatreName;
    this.showBookingModal = true;
  }

  closeBookingModal(): void {
    this.showBookingModal = false;
  }

  onBookingSuccess(): void {
    this.closeBookingModal();
    this.getAllMovies(); // refresh ticket count
  }
}
