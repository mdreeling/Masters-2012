class CreateMovieReviews < ActiveRecord::Migration
  def change
    create_table :movie_reviews do |t|
      t.text :reviewcontent
      t.integer :user_id
      t.string :reviewtitle
      t.integer :starrating
      t.string :rtlink
      t.string :genre
      t.string :movietitle
      t.datetime :revieweddate
      t.datetime :dvdreleasedate
      t.string :mediatype
      t.string :mclink
      t.string :mcratinginteger
      t.integer :rtrating
      t.boolean :theaterrelease

      t.timestamps
    end
    add_index :movie_reviews, [:user_id, :revieweddate]  # EXTRA LINE
  end
end
