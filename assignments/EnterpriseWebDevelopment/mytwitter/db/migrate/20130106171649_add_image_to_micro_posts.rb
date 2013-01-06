class AddImageToMicroPosts < ActiveRecord::Migration
  def change
    add_column :micro_posts, :image, :string

  end
end
