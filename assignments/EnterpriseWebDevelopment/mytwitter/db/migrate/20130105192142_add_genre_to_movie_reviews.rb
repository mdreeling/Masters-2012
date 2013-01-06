class AddGenreToMovieReviews < ActiveRecord::Migration
  def change
    add_column :movie_reviews, :genre_id, :string

  end
end
