class MovieReview < ActiveRecord::Base
  attr_accessible :reviewcontent
  belongs_to :user    # Indicates association with User

  validates :user_id, presence: true
  validates :reviewcontent, presence: true, length: { maximum: 5000 }
  default_scope order: 'movie_reviews.created_at DESC'
end
