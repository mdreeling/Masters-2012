class AddMediaToMovieReviews < ActiveRecord::Migration
  def change
    add_column :movie_reviews, :media_id, :string

  end
end
