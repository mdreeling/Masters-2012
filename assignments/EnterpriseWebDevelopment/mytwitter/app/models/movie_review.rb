class MovieReview < ActiveRecord::Base
  attr_accessible :reviewcontent,:genre, :reviewtitle, :genre_id, :movietitle, :revieweddate, :dvdreleasedate, :media_id, :theaterrelease, :starrating, :image
  belongs_to :user    # Indicates association with User
    #This should go in your model
  mount_uploader :image, ImageUploader
  
  validates :user_id, presence: true
  validates :reviewtitle, presence: true , length: { maximum: 250 }
  validates :movietitle, presence: true, length: { maximum: 100 }
  validates :revieweddate, presence: true
  validates :dvdreleasedate, presence: true
  validates :media_id, presence: true
  validates :theaterrelease, presence: true
  validates :reviewcontent, presence: true, length: { maximum: 5000 }
  default_scope order: 'movie_reviews.created_at DESC'
  belongs_to  :genre
  belongs_to  :medium
  letsrate_rateable "starrating"
end
