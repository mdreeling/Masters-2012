class User < ActiveRecord::Base
  attr_accessible :name, :email, :password, :password_confirmation
  has_secure_password

  # Replace current before_save code with the following:
  before_save do |user| 
        user.email = email.downcase 
        user.remember_token = SecureRandom.urlsafe_base64
        end
   # End of replacement

  validates :name, presence: true, length: { maximum: 50 }
  VALID_EMAIL_REGEX = /\A[\w+\-.]+@[a-z\d\-.]+\.[a-z]+\z/i
  validates :email, presence:   true,
                    format:     { with: VALID_EMAIL_REGEX },
                    uniqueness: { case_sensitive: false }
  validates :password, presence: true, length: { minimum: 6 }
  validates :password_confirmation, presence: true
  
  has_many :microposts, dependent: :destroy   #  CHANGED
  has_many :movie_reviews, dependent: :destroy   #  CHANGED
end