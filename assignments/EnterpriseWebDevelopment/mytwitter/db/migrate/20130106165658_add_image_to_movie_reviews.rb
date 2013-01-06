class AddImageToMovieReviews < ActiveRecord::Migration
  def change
    add_column :movie_reviews, :image, :string

  end
end
