class User < ActiveRecord::Base
  has_secure_password
  attr_accessible :name, :email, :password, :password_confirmation

  has_many :microposts, dependent: :destroy   #  CHANGED
  has_many :movie_reviews, dependent: :destroy   #  CHANGED
  has_many :relationships, foreign_key: "follower_id", dependent: :destroy
  has_many :followed_users, through: :relationships, source: :followed

  has_many :reverse_relationships, foreign_key: "followed_id",
                                   class_name:  "Relationship",
                                   dependent:   :destroy
  has_many :followers, through: :reverse_relationships, source: :follower
  has_many :following, :through => :relationships, :source => :followed

  validates :name, presence: true, length: { maximum: 50 }
  VALID_EMAIL_REGEX = /\A[\w+\-.]+@[a-z\d\-.]+\.[a-z]+\z/i
  validates :email, presence:   true,
                    format:     { with: VALID_EMAIL_REGEX },
                    uniqueness: { case_sensitive: false }
  validates :password, presence: true, length: { minimum: 6 }
  validates :password_confirmation, presence: true
  def feed
    Micropost.from_users_followed_by(self)
  end

  # Replace current before_save code with the following:
  before_save do |user|
    user.email = email.downcase
    user.remember_token = SecureRandom.urlsafe_base64
  end
  # End of replacement

  def follow!(other_user)
    relationships.create!(followed_id: other_user.id)
  end

  def unfollow!(other_user)
    relationships.find_by_followed_id(other_user.id).destroy
  end

  def following?(other_user)
    relationships.find_by_followed_id(other_user.id)
  end

  letsrate_rater
end