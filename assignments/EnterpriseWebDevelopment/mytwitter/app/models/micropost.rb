class Micropost < ActiveRecord::Base
  attr_accessible :content,:image
  belongs_to :user    # Indicates association with User
  mount_uploader :image, ImageUploader
  validates :user_id, presence: true
  validates :content, presence: true, length: { maximum: 140 }
  default_scope order: 'microposts.created_at DESC'

  scope :from_users_followed_by, lambda { |user| followed_by(user) }
  
  private
  
    def self.followed_by(user)
      following_ids = %(SELECT followed_id FROM relationships
                        WHERE follower_id = :user_id)
      where("user_id IN (#{following_ids}) OR user_id = :user_id",
            :user_id => user)
    end
end