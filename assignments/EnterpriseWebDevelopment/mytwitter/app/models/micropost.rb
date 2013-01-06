class Micropost < ActiveRecord::Base
  attr_accessible :content,:image
  belongs_to :user    # Indicates association with User
  mount_uploader :image, ImageUploader
  validates :user_id, presence: true
  validates :content, presence: true, length: { maximum: 140 }
  default_scope order: 'microposts.created_at DESC'
end