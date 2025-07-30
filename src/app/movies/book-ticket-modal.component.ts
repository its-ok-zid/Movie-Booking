import { Component, Input, Output, EventEmitter } from '@angular/core';
import { MovieService } from '../core/services/movie.service';

@Component({
  selector: 'app-book-ticket-modal',
  templateUrl: './book-ticket-modal.component.html',
  styleUrls: ['./book-ticket-modal.component.scss']
})
export class BookTicketModalComponent {
  @Input() movieName!: string;
  @Input() theatreName!: string;
  @Input() totalTickets!: number;
  @Output() closed = new EventEmitter<void>();
  @Output() booked = new EventEmitter<void>();


  seatRows = ['A', 'B', 'C', 'D', 'E', 'F'];
  seatCols = Array.from({ length: 10 }, (_, i) => (i + 1).toString());
  selectedSeats: string[] = [];
  bookedSeats: string[] = [];
  isBooking = false;
  errorMessage = '';
  successMessage = '';

  ngOnInit(): void {
    // Block seats so only totalTickets are available
    const allSeats: string[] = [];
    for (const row of this.seatRows) {
      for (const col of this.seatCols) {
        allSeats.push(row + col);
      }
    }
    const bookedCount = Math.max(0, allSeats.length - (this.totalTickets || 0));
    this.bookedSeats = this.getRandomSeats(allSeats, bookedCount);
  }

  getRandomSeats(seats: string[], count: number): string[] {
    const shuffled = seats.slice();
    for (let i = shuffled.length - 1; i > 0; i--) {
      const j = Math.floor(Math.random() * (i + 1));
      [shuffled[i], shuffled[j]] = [shuffled[j], shuffled[i]];
    }
    return shuffled.slice(0, count);
  }

  isBooked(seat: string): boolean {
    return this.bookedSeats.includes(seat);
  }

  constructor(private movieService: MovieService) {}


  isSelected(seat: string): boolean {
    return this.selectedSeats.includes(seat);
  }


  toggleSeat(seat: string) {
    if (this.isBooked(seat)) return; // can't select booked seat
    if (this.isSelected(seat)) {
      this.selectedSeats = this.selectedSeats.filter(s => s !== seat);
    } else {
      this.selectedSeats.push(seat);
    }
  }


  bookTickets() {
    if (this.selectedSeats.length === 0) return;
    this.isBooking = true;
    this.errorMessage = '';
    this.successMessage = '';
    const payload = {
      movieName: this.movieName,
      theatreName: this.theatreName,
      numberOfTickets: this.selectedSeats.length,
      seatNumbers: this.selectedSeats.join(', ')
    };
    this.movieService.bookTicket(payload).subscribe({
      next: () => {
        this.successMessage = 'Booking successful!';
        // After booking, add selected seats to bookedSeats
        this.bookedSeats = [...this.bookedSeats, ...this.selectedSeats];
        this.selectedSeats = [];
        this.isBooking = false;
        this.booked.emit();
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Booking failed.';
        this.isBooking = false;
      }
    });
  }

  close() {
    this.closed.emit();
  }
}
