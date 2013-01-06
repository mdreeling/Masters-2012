class Genre < ActiveRecord::Base
  has_many :movie_reviews
end
